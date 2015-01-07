@echo off

REM ============================= PARAMETRES UTILES =====================================

REM Chemin du projet
set COMPARE=D:\Compare

REM JDK
set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_67

set COMPARE_CP=D:\Compare\lib\*

REM == On conserve la date-heure de début
set DATEDEB=%DATE%
set TIMEDEB=%TIME%

REM =====================================================================================


REM Verifions la bonne installation de JAVA
if not exist "%JAVA_HOME%\bin\java.exe" goto nokJava
if not exist "%COMPARE%" goto nokHome

echo == M0.0: Conversion des fichiers excels en csv ==


echo == conversion des fichiers excels en csv ==
"%JAVA_HOME%\bin\java" -Xms128M -Xmx512M -Dfile.encoding=UTF-8 -classpath %COMPARE_CP% ps.pcbs.compare.utils.ExcelExporter

echo CodeRetour : %ErrorLevel%
rem if %ErrorLevel%==202 goto FinErreur
date /t
time /t

pause

goto FinOK

:FinErreur
echo =========================================================================
echo Date deb: %DATEDEB% - %TIMEDEB%
echo Date fin: %DATE% - %TIME%
echo =========================================================================
echo ==== ERREUR ERREUR ERREUR ERREUR ERREUR ERREUR ERREUR ERREUR ERREUR  ====
echo =========================================================================

:FinOK
echo =========================================================================
echo ====     Fin avec succès  ====
echo =========================================================================
goto end


:nokJava
echo
echo La variable d'environnement JAVA_HOME n'est pas correctement definie.

echo Cette variable d'environnement est necessaire pour demarrer cette application.

echo Aller dans [Panneau de configuration] puis [Systeme]

echo Onglet [Avance] puis [Variables d'environnement]

echo Bouton [Nouveau]  == Variables systeme ==

echo Par exemple :
echo Nom de la variable : JAVA_HOME
echo Valeur de la variable : C:\Program Files\Java\jre1.5.0_05

goto end


:nokHome
echo
echo La variable d'environnement ESTEL_BATCH n'est pas correctement definie.

echo Cette variable d'environnement est necessaire pour demarrer cette application.

echo Aller dans [Panneau de configuration] puis [Systeme]

echo Onglet [Avance] puis [Variables d'environnement]

echo Bouton [Nouveau]  == Variables systeme ==

echo Par exemple :
echo Nom de la variable : ESTEL_BATCH_HOME
echo Valeur de la variable : D:\gbh0hz\Mes Documents\eclipse_workspace\estelBatch
goto end


:end
