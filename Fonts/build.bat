@echo off
echo Netejant..
rmdir build /S /Q
mkdir build
echo Compilant..
cd src
javac ./prop/*.java -d ../build -Xlint:unchecked -encoding utf8
xcopy /F /Y /I "./prop/header.jpg" "../build/prop/"
xcopy /E /I "com" "../build/com"
xcopy /E /I "META-INF" "../build/META-INF"
cd ../build
jar cmf META-INF/MANIFEST.MF App.jar *
echo Presioni una tecla per executar l'aplicacio compilada
pause>nul
java -jar App.jar