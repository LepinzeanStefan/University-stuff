// SPDX-License-Identifier: BSD-3-Clause
#include <unistd.h>
#include <string.h>

/*  Writes str to STDOUT up to the NULL terminator, followed by \n.
 *	Return non-negative value on succes. On error, returns -1
 */
int puts(const char *str)
{
	int ret_value = write(1, str, strlen(str));

	if (ret_value != -1)
		write(1, "\n", 1);
	return ret_value;
}
