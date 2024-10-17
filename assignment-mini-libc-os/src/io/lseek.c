// SPDX-License-Identifier: BSD-3-Clause

#include <unistd.h>
#include <internal/syscall.h>
#include <errno.h>
#include <internal/types.h>

off_t lseek(int fd, off_t offset, int whence)
{
	// 8 is the value of the lseek syscall
	int ret_code = syscall(__NR_lseek, fd, offset, whence);
	if (ret_code < 0) {
		errno = -ret_code;
		return -1;
	}

	return ret_code;
}
