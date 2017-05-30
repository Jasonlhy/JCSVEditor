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

Note:

- Excel can display the data with correct encoding using [Import from Text approach](https://superuser.com/questions/280603/how-to-set-character-encoding-when-opening-excel)
- Or PowerShell in windows: `Import-Csv .\utf8.csv | Out-GridView`

# Known issue
- The input file should be a valid csv format, and no leading and no trailing empty row.
- The field cannot contain line breaks