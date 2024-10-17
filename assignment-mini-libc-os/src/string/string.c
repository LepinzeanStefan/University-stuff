// SPDX-License-Identifier: BSD-3-Clause

#include <string.h>

char *strcpy(char *destination, const char *source)
{
	char *copy = destination;
	while (*source) {
		*copy = *source;
		source++;
		copy++;
	}
	*copy = *source;

	return destination;
}

char *strncpy(char *destination, const char *source, size_t len)
{
	char *copy = destination;
	for (size_t count = 0; *source && count < len; source++, copy++, count++) {
		*copy = *source;
	}
	*copy = *source;

	return destination;
}

char *strcat(char *destination, const char *source)
{
	char *copy = destination;
	while (*copy) {
		copy++;
	}

	copy = strcpy(copy, source);

	return destination;
}

char *strncat(char *destination, const char *source, size_t len)
{
	char *copy = destination;
	while (*copy) {
		copy++;
	}

	for (size_t count = 0; *source && count < len; source++, copy++, count++) {
		*copy = *source;
	}
	*copy = '\0';

	return destination;
}

int strcmp(const char *str1, const char *str2)
{
	while (*str1 == *str2)
	{
		if(*str1 == '\0')
			return *str1 - *str2;
		str1++;
		str2++;
	}

	return *str1 - *str2;
}

int strncmp(const char *str1, const char *str2, size_t len)
{
	size_t count = 0;
	while (*str1 == *str2 && count < len)
	{
		if (*str1 == '\0')
			return *str1 - *str2;
		str1++;
		str2++;
		count++;
	}

	if (count == len)
		return 0;

	return *str1 - *str2;
}

size_t strlen(const char *str)
{
	size_t i = 0;

	for (; *str != '\0'; str++, i++)
		;

	return i;
}

char *strchr(const char *str, int c)
{
	for (char *first_occur = str; *first_occur; first_occur++) {
		if (*first_occur == c) {
			return first_occur;
		}
	}
	return NULL;
}

char *strrchr(const char *str, int c)
{
	char *last_occur = NULL;
	for (; *str; str++) {
		if (*str == c) {
			last_occur = str;
		}
	}
	return last_occur;
}

char *strstr(const char *haystack, const char *needle)
{
	char *original = needle;
	char *ret_point = NULL;
	while(*haystack) {
		if (*haystack == *needle) {
			ret_point = haystack;
			while (*needle && *haystack)
			{
				haystack++;
				needle++;
				if(*haystack != *needle) {
					break;
				}
			}
			if (!*needle) {
				return ret_point;
			}
			needle = original;
		}
		haystack++;
	}
	return NULL;
}

char *strrstr(const char *haystack, const char *needle)
{
	char *original = needle;
	char *ret_point = NULL;
	char *last_copy = NULL;
	while(*haystack) {
		if (*haystack == *needle) {
			last_copy = ret_point;
			ret_point = haystack;
			while (*needle && *haystack)
			{
				haystack++;
				needle++;
				if(*haystack != *needle) {
					break;
				}
			}
			if (*needle) {
				ret_point = last_copy;
			}
			needle = original;
		}
		haystack++;
	}
	return ret_point;
}

void *memcpy(void *destination, const void *source, size_t num)
{
	void *copy = destination;
	for (size_t iter = 0; iter < num; iter++) {
		*(char*)copy = *(const char*)source;
		copy++;
		source++;
	}
	return destination;
}

void *memmove(void *destination, const void *source, size_t num)
{
	void *copy = destination;
	if (destination == source) {
		return destination;
	}
		if (destination < source){
		// If dest is placed before src, use memcpy
		destination = memcpy(destination, source, num);
	} else {
		// If dest is placed after src, simply copy the memory backwards, from the end
		copy += num - 1;
		source += num - 1;
		while (num) {
			*(char *)copy = *(const char*)source;
			copy--;
			source--;
			num--;
		}
	}
	return destination;
}

int memcmp(const void *ptr1, const void *ptr2, size_t num)
{
	const char *char1 = (const char *)ptr1;
	const char *char2 = (const char *)ptr2;
	while (num) {
		if(*char2 != *char1) {
			return *char1 - *char2;
		}
		char1++;
		char2++;
		num--;
	}
	return 0;
}

void *memset(void *source, int value, size_t num)
{
	unsigned char *copy = (unsigned char *)source;
	for (size_t i = 0; i < num; i++) {
		*(copy + i) = (unsigned char)value;
	}
	return source;
}
