#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_INPUT_LEN 256

typedef char Item;
#include "Stack.h"

int isBalanced(const char *str, int length){

  /* TODO: Cerinta 3
   * Implementation must use a stack.
   * Do NOT forget to deallocate the memory you use.
   */
    int ret = 1, i = 0;
    Stack* stiva = createStack();
    for(; i < length; i++) {
      if(strchr("([{", str[i])) {
        push(stiva, str[i]);
      }
      else {
        char ch;
        if(isStackEmpty(stiva)) {
          ret = 0;
          break;
        }
        if(str[i] == ')')
          ch = '(';
        if(str[i] == ']')
          ch = '[';
        if(str[i] == '}')
          ch = '{';
        if(ch != stiva->head->elem) {
          ret = 0;
          break;
        }
        pop(stiva);
      }
    }
    if(stiva->head != NULL) {
      ret = 0;
    }
    destroyStack(stiva);
    return ret;
}

int main(){
    int len;
    char buffer[MAX_INPUT_LEN];
    FILE* inputFile = fopen("input-parantheses.txt","r");
    if(inputFile == NULL) return 1;


    while(fgets(buffer, MAX_INPUT_LEN, inputFile) != NULL){
      buffer[strcspn(buffer, "\r\n")] = 0;
      len = strlen(buffer);
      if(len == 0) break;

      if(isBalanced(buffer, len))
        printf("%s ---> is balanced.\n", buffer);
      else
        printf("%s ---> not balanced.\n", buffer);
    }

    fclose(inputFile);

    return 0;
}
