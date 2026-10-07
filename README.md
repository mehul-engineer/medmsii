# MEMSII

### A small, table-driven experiment in reversible text-to-bit encoding

MEMSII maps supported characters to fixed-width binary codewords, joins those codewords into a bitstream, adds random framing bits, and complements the result. `Decoder` reverses those steps and maps the recovered values back to characters.

The project is intentionally compact: the character/code table lives in one base class, while encoding and decoding are separate Java classes. It is a useful starting point for exploring lookup tables, bit manipulation, framing, and the difference between encoding and cryptography.

> **Security note:** MEMSII is an educational encoding/obfuscation experiment, not encryption. It has no secret key and provides no confidentiality, integrity, or authentication. Do not use it to protect passwords, personal data, or other sensitive information.

## What happens to a character?

For each input character, the encoder:

1. Looks up its integer in `encodedArray` using the corresponding position in `charArray`.
2. Converts that integer to binary.
3. Appends `00` and left-pads the result to a 10-bit codeword.
4. Concatenates the codewords for the whole input.
5. Adds five random bits at the front and seven at the end.
6. Complements each bit (`0` becomes `1`, and `1` becomes `0`).

The decoder complements the stream, removes the five-bit prefix and seven-bit suffix, splits the remaining stream into 10-bit codewords, and uses the lookup table to recover characters.

```text
supported text
     │
     ▼
character lookup → 10-bit codewords → concatenate
                                      │
                                      ▼
                         5-bit pad + 7-bit pad
                                      │
                                      ▼
                              bit complement
                                      │
                                      ▼
                              encoded bitstring
```

The framing bits are discarded during decoding; they are not a key or a security boundary. Because the encoder creates them randomly, encoding the same input twice can produce different bitstrings while decoding to the same text.

## Repository layout

| File | Responsibility |
| --- | --- |
| `memsii.java` | Shared character and integer lookup tables used by both directions. |
| `Encoder.java` | Character lookup, binary formatting, random framing, and bit complement. |
| `Decoder.java` | Framing removal, codeword parsing, and reverse lookup. |
| `LICENSE` | GNU General Public License, version 3. |

Class names retain the capitalization used in the source files. Compile from the repository root so Java can resolve the shared base class.

## Requirements

- A Java Development Kit (JDK) available on your `PATH`.
- No third-party libraries or build tool are required.

Check your Java installation:

```bash
java -version
javac -version
```

## Build

```bash
javac memsii.java Encoder.java Decoder.java
```

This produces `.class` files in the current directory. To remove them afterward:

```bash
rm -f ./*.class
```

## Try the included entry points

The encoder's `main` method encodes the sample string `Amritesh` and prints a bitstring:

```bash
java Encoder
```

The decoder's `main` method decodes a bitstring embedded in `Decoder.java` and prints its result:

```bash
java Decoder
```

These are demonstration entry points, not a command-line interface: neither program currently accepts input arguments or reads from standard input.

## Encode and decode your own text

The classes expose `getEncoded(String)` and `getDecoded(String)`. The following temporary Java program demonstrates using them without changing the project sources:

```java
public class RoundTrip {
    public static void main(String[] args) {
        String original = "Hello, MEMSII!";

        Encoder encoder = new Encoder();
        Decoder decoder = new Decoder();

        String bits = encoder.getEncoded(original);
        String recovered = decoder.getDecoded(bits);

        System.out.println("Original:  " + original);
        System.out.println("Bitstream: " + bits);
        System.out.println("Recovered: " + recovered);
    }
}
```

Save it as `RoundTrip.java`, then compile and run:

```bash
javac memsii.java Encoder.java Decoder.java RoundTrip.java
java RoundTrip
```

Use characters represented by the tables in `memsii.java`. The current implementation does not define a general Unicode or byte-oriented format, and unsupported characters are not handled with a clear validation error. Keep inputs to the listed character set.

## Format at a glance

| Stage | Current behavior |
| --- | --- |
| Symbol representation | Lookup-table integer rendered as binary, followed by `00`, padded to 10 bits. |
| Message layout | Fixed-width codewords concatenated in input order. |
| Framing | Five random leading bits and seven random trailing bits. |
| Final transform | Bitwise complement of the framed string. |
| Decode strategy | Reverse the complement and framing, then linearly search the integer table for each codeword. |

The implementation is string-based: it prints and parses characters `'0'` and `'1'`; it does not pack the result into bytes. Its framing and codeword conventions are implementation details of this source version, not a published or standardized interchange format.

## Design notes and current boundaries

- **Reversible mapping, not cryptography.** The lookup table is embedded in the program and the bit complement is trivially reversible. Random framing does not make the content secret.
- **No integrity check.** A modified or truncated bitstream is not authenticated. The decoder does not report structured parse errors for malformed input.
- **Restricted character support.** The format depends on the paired arrays in `memsii.java`; it is not a general Unicode encoding.
- **Simple implementation choices.** The source uses `java.util.Random`, repeated string concatenation, and linear searches. These make the code approachable, but are not intended as performance or security recommendations.
- **No stability promise.** Changing either lookup table or the framing convention changes compatibility with previously generated bitstrings.

These boundaries make MEMSII best suited to learning, experimentation, and code-reading exercises. If extending it, useful next steps include validating the tables and input, defining a versioned byte-level format, reporting malformed streams explicitly, and adding round-trip tests over every supported character.

## License

This project is distributed under the [GNU General Public License v3.0](LICENSE). See `LICENSE` for the complete terms.
