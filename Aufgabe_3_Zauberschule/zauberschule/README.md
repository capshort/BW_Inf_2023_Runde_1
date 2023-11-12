# zauberschule

Solution to Task 3 from [Bundeswettbewerb Informatik Runde 42](https://bwinf.de/fileadmin/bundeswettbewerb/42/BwInf_42_Aufgaben_WEB.pdf).

## Usage

To run the code you can set up [Leiningen](https://leiningen.org).
After that use a console, navigate to a folder you deem appropriate and type in these commands:

    $ git checkout https://github.com/capshort/BW_Inf_2023_Runde_1.git
    $ cd BW_Inf_2023_Runde_1/Aufgabe_3_Zauberschule/zauberschule/
    $ lein uberjar
    $ cd target/uberjar
    $ java -jar zauberschule-0.1.0-standalone.jar [-v] <file_path>

## Options

* -v Toggle on progress information (default: no progress information)

## License

Copyright © 2023 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
