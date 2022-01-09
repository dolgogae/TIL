symbols = '$%^&*'
beyond_ascii = [ord(s) for s in symbols if ord(s) > 127]
beyond_ascii

beyond_ascii = list(filter(lambda c: c > 127, map(ord, symbols)))
beyond_ascii

# ---------------------------------------------

colors = ['black', 'white']
sizes = ['S', 'M', 'L']
for tshirts in ( '%s %s' % (c, s) for c in colors for s in sizes):
	print(tshirts)

# ---------------------------------------------

lax_coordinates = (33.9425, -118.34245)
city, year, pop, chg, area = ('Tokyo', 2003, 32450, 0.66, 8014)
traveler_ids = [('USA', '32423655', ('BRA', 'CE1234'), ('ESP', 'XDA425546')]

for passport in sorted(traveler_ids):
	print('%s/%s' % passport)

for country, _ in traveler_ids:
	print(country)

# --------------------------------------------

divmod(20, 8)
t = (20, 8)
divmod(*t)
quotient, remainder = divmod(*t)

# 튜플은 하나의 정보를 담고있는 레코드이고 *를 이용해서 언팩해준다

# --------------------------------------------

# (a, b, (c, d)) 언팩하기

for name, cc, pop, (latitude, longtitude) in metro_areas:
	'''
	something
	'''

# --------------------------------------------

import collections import namedtuple

City = namedtuple('City', 'name country population coordinates')
tokyo = City('Tokyo', 'JP', 36.933, (1234, 3243))

print(tokyo)
print(tokyo.coordinates)

LatLong = namedtuple('LatLong', 'lat long')
delhi_data = ('Delhi NCR', 'IN', 21.935, LatLong(28.613889, 77.208889))

delhi = City._make(delhi_data)

delhi._asdict()

for key, value in delhi._asdict().items():
	print(key + ':', value)
