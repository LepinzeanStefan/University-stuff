// SPDX-License-Identifier: BSD-3-Clause

#include <sys/mman.h>
#include <errno.h>
#include <internal/syscall.h>

void *mmap(void *addr, size_t length, int prot, int flags, int fd, off_t offset)
{
	long ret_code = syscall(__NR_mmap, addr, length, prot, flags, fd, offset);

	if (ret_code < 0) {
		errno = -ret_code;
		return MAP_FAILED;
	}
	return (void *)ret_code;
}

void *mremap(void *old_address, size_t old_size, size_t new_size, int flags)
{
	long ret_code = syscall(__NR_mremap, old_address, old_size, new_size, flags);

	if (ret_code < 0) {
		errno = -ret_code;
		return MAP_FAILED;
	}
	return (void *)ret_code;
}

int munmap(void *addr, size_t length)
{
	int ret_code = syscall(__NR_munmap, addr, length);

	if (ret_code < 0) {
		errno = -ret_code;
		return -1;
	}
	return ret_code;
}
