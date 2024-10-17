// SPDX-License-Identifier: BSD-3-Clause

#include <unistd.h>
#include <internal/syscall.h>
#include <errno.h>

int ftruncate(int fd, off_t length)
{
	// 77 is the value of the fturncate syscall
	int ret_code = syscall(__NR_ftruncate, fd, length);
	if (ret_code < 0) {
		errno = -ret_code;
		return -1;
	}

	return ret_code;
}
