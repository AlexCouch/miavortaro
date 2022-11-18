package net.miavortaro.application.dao

import net.miavortaro.application.model.WordEntryCache
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.EntryUnit
import org.ehcache.config.units.MemoryUnit
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration
import java.io.File

class DAOFacadeCache(val delegate: DAOFacade, val storagePath: File): DAOFacade by delegate{
    val cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerPersistenceConfiguration(storagePath))
        .withCache(
            "vortaroMemoreto",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                String::class.javaObjectType,
                WordEntryCache::class.java,
                ResourcePoolsBuilder.newResourcePoolsBuilder()
                    .heap(1000, EntryUnit.ENTRIES)
                    .offheap(10, MemoryUnit.MB)
                    .disk(100, MemoryUnit.MB, true)
            )
        )
        .build(true)

    val vortaroMemoreto = cacheManager.getCache("vortaroMemoreto", String::class.javaObjectType, WordEntryCache::class.java)

    override fun createWord(word: String, definition: String): String =
        delegate.createWord(word, definition).apply {
            val wordEntry = WordEntryCache(word, definition)
            vortaroMemoreto.put(word, wordEntry)
        }

    override fun getWord(word: String): WordEntryCache =
        vortaroMemoreto.get(word) ?: delegate.getWord(word).apply {
            vortaroMemoreto.put(word, this)
        }

    override fun searchWords(word: String): List<String> =
        vortaroMemoreto.filter { it.key.startsWith(word) }.map { it.key }.let {
            if(it.isEmpty()){
                delegate.searchWords(word)
            }else{
                it
            }
        }

    override fun deleteWord(word: String) =
        vortaroMemoreto.find { it.key == word }.let{
            if(it == null){
                delegate.deleteWord(word)
            }else{
                vortaroMemoreto.remove(word)
            }
        }

    override fun close() =
        cacheManager.use { cacheManager ->
            delegate.close()
        }
}