import sys
from validatelib import *

if __name__ == '__main__':
    sys.exit(ExecutionInfo('assigment example', 'pargrepmon.jar', ['task2.2_strings.txt', 'task2.2_pi.txt'], TextFileInfo('output.txt', '^.*?((42;9980.*?23;9951)|(23;9951.*?42;9980)).*?$'), -1, -1, 'java').run())