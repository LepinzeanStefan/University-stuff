// SPDX-License-Identifier: BSD-3-Clause

#include <sys/stat.h>
#include <internal/syscall.h>
#include <fcntl.h>
#include <errno.h>

int stat(const char *restrict path, struct stat *restrict buf)
{
	// 4 is the value of the stat syscall
	int ret_code = syscall(__NR_stat, path, buf);
	if (ret_code < 0) {
		errno = -ret_code;
		return -1;
	}
	return ret_code;
}
