from miavortaro import MiaVortaro
import pprint

with MiaVortaro() as mv:
    rezulto = mv.listigiVortojn(komenco=0, fino=10)
    pprint.pprint(rezulto)
    