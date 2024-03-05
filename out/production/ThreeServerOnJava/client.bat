@echo off
:loop
java Skeleton.java client "127.0.0.1" 8080 %time:~6,2% %time:~9,2%
goto loop