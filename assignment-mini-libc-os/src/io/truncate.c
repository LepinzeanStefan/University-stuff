// SPDX-License-Identifier: BSD-3-Clause

#include <unistd.h>
#include <internal/syscall.h>
#include <errno.h>

int truncate(const char *path, off_t length)
{
	// 76 is the value of the truncate syscall
	int ret_code = syscall(__NR_truncate, path, length);
	if (ret_code < 0) {
		errno = -ret_code;
		return -1;
	}

	return ret_code;
}
