# Binary quadkeys

As per the Microsoft documentation (https://msdn.microsoft.com/en-us/library/bb259689.aspx),
quadkeys are generally implemented as a string displaying what position in the bing tile system
a the quadkey resides in. This is great because you have an easily human readable geohash that
allows prefix searching to determine whether a higher zoom level quadkey resides inside of a lower
level quadkey. Searching like this is fairly optimized.

Unfortunately, this means that a quadkey takes up a lot of space (due to it being a string).
In particular, at higher zoom levels this space can lead to wasted space and performance
problems when comparing quadkeys to each other.

Due to these problems, it would be nice to have a format that exhibits the same benefits of a
quadkey (prefix searching, geohash) but without the space or performance constraints.

It turns out, because a quadkey has an upper bound on length (23 characters) we can encode a
quadkey into a unsigned 64bit integer. Such an encoding would require packing the quadkey's zoom level into
the integer to preserve the notion of the length of the quadkey. Also because each quadkey is just
an 64 bits, space will be conserved in cases above zoom level 8. Prefix matching can be
acheived as well by doing bitmasking and equality checking (leading to constant time comparisons
instead of linear time comparisons).

## Format

A binary quadkey should be encoded as follows:
* The first (zoom level * 2) bits are the quadkey portion
* Every 2 bits in the quadkey portion represent one character of the quadkey
* The final 5 bits in the number represent the zoom level of the quadkey
* The final 5 bits must be >= 1 and <= 23
* The remaining bits are not used for anything and are undefined

```
quadkey '03120312'
binary quadkey 0b0011011000110110 0000000000000000000000000000000000000000000 01000
                |                |                                           |
                | quadkey bits   |                  undefined bits           | zoom level 8

normal quadkey space = 64 bits (sizeof(char) == 1)
binary quadkey space = 64 bits
```

```
quadkey '02'
binary quadkey 0b0010 0000000000000000000000000000000000000000000000000000000 00010
                |    |                                                       |
                |    |                        undefined bits                 | zoom level 2

normal quadkey space = 16 bits (sizeof(char) == 1)
binary quadkey space = 64 bits
```

```
quadkey '0210320130212302'
binary quadkey 0b00100100111000011100100110110010 000000000000000000000000000 10000
                |                                |                           |
                |           quadkey bits         |       undefined bits      | zoom level 16

normal quadkey space = 128 bits (sizeof(char) == 1)
binary quadkey space = 64 bits
```

## Space comparison

Assumes sizeof(char) == 1, for other char encodings muliply string size by sizeof(char)

* The largest string quadkey (zoom level 23) will take up 23 x 8 bits = 184 bits
* The largest binary quadkey will take up 64 bits
* The largest binary quadkey in decimal is the number `18446744073709289495` which even as a
string is only 20 x 8 bits = 160 bits (though in practice a string version of a binary quadkey
would not be transferred or stored)

* 500 zoom level 15 string quadkeys = 500 x 15 x 8 bits = 7.5 kb
* 500 zoom level 15 binary quadkeys = 500 x 64 bits = 4 kb

* 500 zoom level 20 string quadkeys = 500 x 20 x 8 bits = 10 kb
* 500 zoom level 20 binary quadkeys = 500 x 64 bits = 4 kb

* 3,000,000 zoom level 15 string quadkeys = 3,000,000 x 15 x 8 bits = 360 mb
* 3,000,000 zoom level 15 binary quadkeys = 3,000,000 x 64 bits = 192 mb

## Comparing binary quadkeys

### Same zoom level

For same zoom level quadkeys, a simple integer comparison can be done `qkA ==  qkB`.

### Prefix comparison

To check to see if a quadkey (`qkA`) is contained by another quadkey (`qkB`)
* Extract the zoom level via bitmask `qkBZoomLevel = qkB & 0b11111`
* Convert both quadkeys to prefix `qkPrefixA = qkA >> 64 - qkBZoomLevel` `qkPrefixB = qkB >> 64  - qkBZoomLevel`
* Compare the prefixes `qkPrefixA == qkPrefixB`

## Binary quadkey to string quadkey

```
binary_qk = 2417097 // 0b00100100111000011100100110110010 000000000000000000000000000 10000
zoom_level = binary_qk & 0b11111 //16
str_quadkey = ""
for i = 0; i <= zoom_level; i++ {
  // extract each qk digit
  location = 64 - i * 2
  char_code = (binary_qk & (0b11 << location) >> location)
  str_quadkey += (char)(48 + char_code)
}
```

## Pros vs string quadkeys

* Consistent storage for all quadkeys, always 64 bits
* No dynamic memory storage for quadkeys (super important when dealing with cpu cache efficiency)
* Comparisons can be made in constant time O(1) vs linear time O(n) for string quadkeys
* Faster iteration of a list of quadkeys due to simple array storage and cache optimizations
* Better indexing support in databases for integers vs strings
* Most applications use UTF-8 instead of ASCII encoding which can result in 2x-4x more bloat
for string quadkeys than what is listed in the space comparison section (due to chars being 2
bytes or 4 bytes in UTF-8 or other encodings) **Note** also applicable to languages that default
to UTF-8 string such as Java, Go, or Python 3, Javascript

## Cons vs string quadkeys

* More complicated
* Space wasted for zoom levels < 8
* Languages with no 64bit integers cannot be represented natively (ie Javascript, well, it's
kindof supported but not really...)

## Uses

* Storage or transmission of large amounts of high zoom level quadkeys (ie individual locations
  or a set of quadkeys representing a geometric area at high zoom level)
* Speedy comparison of large amounts of quadkeys
* Speedy iteration of large numbers of quadkeys
