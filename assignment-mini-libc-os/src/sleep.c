// SPDX-License-Identifier: BSD-3-Clause
#include <errno.h>
#include <time.h>

int nanosleep(const struct timespec *requested_time, struct timespec *remaining);

/*  Use nanosleep until secs seconds have passed. The function returns the seconds remaining
 *	if the function fails. Should be 0 on succes
 */
int sleep(unsigned int secs)
{
	struct timespec requested_time, remaining;

	requested_time.tv_sec = secs;
	requested_time.tv_nsec = 0;
	while (nanosleep(&requested_time, &remaining) == -1) {
		if (errno == EINTR)
			requested_time = remaining;
		else
			break;
	}
	return remaining.tv_sec;
}
