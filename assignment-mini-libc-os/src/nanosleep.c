// SPDX-License-Identifier: BSD-3-Clause
#include <internal/syscall.h>
#include <errno.h>
#include <time.h>

/*  Pause execution for a number of seconds. In case of error return -1*/
int nanosleep(const struct timespec *requested_time, struct timespec *remaining)
{
	// 35 is the value for syscall nanosleep
	int ret_value = syscall(__NR_nanosleep, requested_time, remaining);

	if (ret_value < 0) {
		errno = -ret_value;
		return -1;
	}
	return ret_value;
}
