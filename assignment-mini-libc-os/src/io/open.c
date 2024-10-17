// SPDX-License-Identifier: BSD-3-Clause

#include <fcntl.h>
#include <internal/syscall.h>
#include <stdarg.h>
#include <errno.h>

int open(const char *filename, int flags, ...)
{
	int file_desc = 0;
	// Search for the O_CREAT flag using bitwise and
	if ((flags & O_CREAT) == O_CREAT) {
/* 	
	If the O_CREAT flag is present the syscall needs another argument named "mode"
	for the creation of the file. The method of procuring this argument is 
	"heavily inspired" from the solution for this question on stackoverflow:
https://stackoverflow.com/questions/10071186/function-with-unknown-number-of-parameters-in-c 
*/
		va_list list;
		va_start(list, flags);
		mode_t mode = va_arg(list, mode_t);
		va_end(list);
		// 2 is the value of the open syscall
		file_desc = syscall(__NR_open, filename, flags, mode);
	} else {
		file_desc = syscall(__NR_open, filename, flags, 0);
	}

	if (file_desc < 0) {
		errno = -file_desc;
		return -1;
	}
	return file_desc;
}
