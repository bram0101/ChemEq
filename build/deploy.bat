#@echo off
set /p version="Enter version: "
set folder=ChemEq v%version%
"C:\Program Files\Java\jdk1.8.0_144\bin\javapackager" -deploy -native -description "ChemEq is a tool to solve chemical reactions" -name "ChemEq" -title "%folder%" -vendor "Bram Stout" -BappVersion="%version%" -Bicon="icon.ico" -Bidentifier="me.bramstout.chemeq" -Bcopyright="Copyright (c) 2017 Bram Stout" -outdir "%folder%/winpackage" -outfile "%folder%-win" -srcdir "%folder%" -srcfiles "%folder%.jar" -appclass "me.bramstout.chemeq.Main"
pause