moodledump
==========
Moodle public course site generator.
(by Dave Musicant)

This tool is non-robust in nearly every way; I designed it for me to use, but
I'm happy for you to try to do so as well. I've only built in export
capabilities for the portions of Moodle I use, so it may (likely?) crash for you
if you use something I don't. Making into a better tool would probably be
worthwhile.

Here's how to use it:

In Moodle, backup your course. Download the zip file it creates. Sometimes it
has a .mbz extension. Whatever, it's a zip file. Unzip it to the diretory
"dump". The top level files in your Moodle dump (typically a bunch of xml files)
should be sitting right in the dump directory, not a subdirectory.

Sitting in the main directory, run the script "runit". If all goes well, that's
it.


