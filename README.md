# Cobalt
## Info
*Cobalt* is an open-source Minecraft cheating client and mod framework developed by Harry Gallagher (Stl_Missouri) and various other developers.

## Building
* Install [Maven](http://maven.apache.org/)
* Clone the repository
* Execute the [install_local_jars.sh](install_local_jars.sh) script
* Run `mvn clean package`
* You can find the `cobalt-2.0.jar` file in the [target](target/) directory
* Optionally, run the `build_artifacts.sh` script to generate a Minecraft launcher json script and rename the jar to `Cobalt.jar` (for drag-n-drop use with Minecraft)

## Contributing
### Guidelines
Although you are free to modify the Cobalt source however you wish, there are a few restrictions if you would like to create a merge request.

1. Please do not modify any base classes (Cobalt, Minecraft, CobaltPlugin, etc) unless you are fixing a bug in one of them
2. Please do not make unnecessary amounts of changes (code reformats, etc.) to files you otherwise wouldn't have edited
3. Please cite yourself as author in any classes you create

## License
Cobalt is licensed under the Do What The Fuck You Want To Public License version 2.

>            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
>                    Version 2, December 2004
>
> Copyright (C) 2014 Harry Gallagher \<harrygallagher4@gmail.com>
>
> Everyone is permitted to copy and distribute verbatim or modified
> copies of this license document, and changing it is allowed as long
> as the name is changed.
>
>            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
>   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
>
>  0. You just DO WHAT THE FUCK YOU WANT TO.
