"# threeServerOnJava" 


В IntelIJ с помощью комбинации клавиш CTRL+ALT+SHIFT+S попадаем в структуру проекта. 
Заходим в пункт Artifacts Нажимаем на Add (зеленый плюс), 
выбираем JAR и пункт from modules with dependencies. 
В выплывающем окне выбираем главный класс.
Жмем ОК, закрываем окно. 
Выскочит еще одно окно, в котором нажимаем Apply, закрываем это окно. 
Настройка закончена. 
Идем в пункт Build Выбираем Build Artifact, затем Build.

Исполняемый файл будет сгенерирован в директории out / Artifacts.
Могут возникнуть проблемы если в проекте есть import какого-либо пакета. но этого импорта нет в classpath
~~~~Если JAR не запускается, войдите в структуру проекта, пункт Modules -> Dependencies -> Add -> Project Libruary -> Attach JAR
