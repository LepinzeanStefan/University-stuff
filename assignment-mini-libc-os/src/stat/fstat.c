// SPDX-License-Identifier: BSD-3-Clause

#include <sys/stat.h>
#include <internal/syscall.h>
#include <errno.h>

int fstat(int fd, struct stat *st)
{
	// 5 is the value of the fstat syscall
	int ret_code = syscall(__NR_fstat, fd, st);
	if (ret_code < 0) {
		errno = -ret_code;
		return -1;
	}

	return ret_code;
}
