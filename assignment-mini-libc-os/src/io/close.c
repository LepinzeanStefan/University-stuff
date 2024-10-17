// SPDX-License-Identifier: BSD-3-Clause

#include <unistd.h>
#include <internal/syscall.h>
#include <stdarg.h>
#include <errno.h>

int close(int fd)
{
	// 3 is the value of the close syscall
	int	ret_code = syscall(__NR_close, fd);
	if (ret_code < 0) {
		errno = -ret_code;
		return -1;
	}

	return ret_code;
}
