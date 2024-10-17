// SPDX-License-Identifier: BSD-3-Clause

#include <internal/mm/mem_list.h>
#include <internal/types.h>
#include <internal/essentials.h>
#include <sys/mman.h>
#include <string.h>
#include <stdlib.h>

// Implmentation inspired by https://github.com/Jibus22/malloc/tree/main

void *malloc(size_t size)
{
	// As mentioned in https://man7.org/linux/man-pages/man2/mmap.2.html, it is
	// recommended to set the file descriptor to -1 when using MAP_ANON
	void *ptr = mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);

	if (ptr == MAP_FAILED) {
		return NULL;
	}
	mem_list_add(ptr, size);

	return ptr;
}

void *calloc(size_t nmemb, size_t size)
{
	size_t full_size = nmemb * size;
	void *ptr = malloc(full_size);

	memset(ptr, 0, full_size);

	return ptr;
}

void free(void *ptr)
{
	struct mem_list *mem_item = mem_list_find(ptr);

	if (mem_item) {
		munmap(mem_item->start, mem_item->len);
		mem_list_del(mem_item->start);
	}
}

void *realloc(void *ptr, size_t size)
{
	struct mem_list *mem_item = mem_list_find(ptr);

	if (mem_item) {
		void *new_ptr = malloc(size);
		memcpy(new_ptr, ptr, mem_item->len);
		free(mem_item->start);
		mem_list_add(new_ptr, size);
		return new_ptr;
	}

	return NULL;
}

void *reallocarray(void *ptr, size_t nmemb, size_t size)
{
	if (__SIZE_MAX__ / size < nmemb) {
		return NULL;
	}

	ptr = realloc(ptr, nmemb * size);
	return ptr;
}
