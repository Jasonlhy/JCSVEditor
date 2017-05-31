# JCSVEditor
Modified from past GUI program assignment.

Getting struggle with Excel which removes the leading zero by default, and encoding problems.
I just want to have a lightweight CSV editor which displays the raw data and have a file explorer.

# System Requirement
1. Java Runtime Environment 8

# Advantage over Excel
1. Directly open with UTF-8 encoding, and any encodings that JRE supports
2. Do not lock the file, just refresh the file.
3. Raw text of the csv file, no formatting so no more removal of leading zero.
4. Supports cell value with line breaks
5. Supports all common line breaks
    - Windows: `\r\n`
    - Mac: `\r`
    - Linux: `\n`

Note:

- Excel can display the data with correct encoding using [Import from Text approach](https://superuser.com/questions/280603/how-to-set-character-encoding-when-opening-excel)
- Or PowerShell in windows (I think this only supports utf8): `Import-Csv .\utf8.csv | Out-GridView`

# The file format It try to read
- `,`, line break should be embedded inside `"` and `"`
- `"` should be escaped inside `"` and `"` as `""`, 

## Example
- `This is "great performance` 
    => `"This is ""great performance"`

# Known issue
- The input file should be a [valid csv format](https://tools.ietf.org/html/rfc4180), with no leading and no trailing empty row.
- Problem with saving utf 8 with bom file, the invisible byte order mask is embedded in the cell value, need to remove the [byte order mask]. (https://en.wikipedia.org/wiki/Byte_order_mark)
- The line break embedded inside double always is `\n`

# Enhancement Planning
- Better error handling
- Remove the byte order mask with saving utf 8 with bom