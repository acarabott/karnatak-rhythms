/* 
integer partitioning algorithm, implemented from 

Fast Algorithms for Generating Integer Partitions by Antoine Zoghbi and Ivan Stojmenovic
https://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.42.1287&rep=rep1&type=pdf


## Compiling

should compile with any C compiler, e.g.
```
$ clang zs1-partition.c
```

will generate a.out (default name)

## Usage (all partitions of 10)
./a.out 10
 */

#include <stdlib.h>
#include <stdio.h>

void zs1(size_t n, FILE *fd)
{
  size_t i, m, h, r, t;
  size_t *x;

  x = malloc(sizeof(*x)*(n+1));

  for (i = 1; i <= n; i++)
    x[i] = 1;

  x[1] = n;
  m = 1;
  h = 1;
  fprintf(fd,"%zd\n",x[1]);

  while (x[1] != 1) {
    if (x[h] == 2) {
      m = m + 1;
      x[h] = 1;
      h = h - 1;
    } else {
      r = x[h] - 1;
      t = m - h + 1;
      x[h] = r;

      while (t >= r) {
        h = h + 1;
        x[h] = r;
        t = t -r;
      }

      if (t == 0) {
        m = h;
      } else {
        m = h + 1;
        if (t > 1) {
          h = h + 1;
          x[h] = t;
        }
      }
    }

    for (i = 1; i <= m; i++)
      fprintf(fd,"%zd ",x[i]);
    fprintf(fd,"\n");
  }

  free(x);
}

int main(int argc, char**argv)
{

    int first_arg = 0;
    if(argc > 1) {
      first_arg = atoi(argv[1]);
    } else {
      printf("must pass integer as argument\n");
      return 0;
    }

    // printf("Partitioning: %d\n", first_arg);

    zs1(first_arg, stdout);
	return 0;
}
