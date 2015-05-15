@echo off
pushd .
::set branch=feature_140228_hybrid
::set product=gokivo

if not exist %ANT_HOME%\bin\ant.bat goto :ERR

call ant -Dbuild.android=true -Dbuild.debug_network=true
::call ant -Dbuild.branch=%branch% -Dbuild.product=%product% build
goto :END

:ERR
echo  === Need to set ANT_HOME =======
:END
popd