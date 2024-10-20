## Copyright (C) 2023 Andrei
##
## This program is free software: you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation, either version 3 of the License, or
## (at your option) any later version.
##
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
##
## You should have received a copy of the GNU General Public License
## along with this program.  If not, see <https://www.gnu.org/licenses/>.

## -*- texinfo -*-
## @deftypefn {} {@var{retval} =} task2 (@var{input1}, @var{input2})
##
## @seealso{}
## @end deftypefn

## Author: Andrei <Andrei@DESKTOP-PK505U9>
## Created: 2023-02-28

function new_X = task2 (photo, pcs)
  [m n] = size(photo);
  
  % initializare matrice finala.
  new_X = zeros(m, n);
  
  % cast photo la double.
  photo = double(photo);
  
  % normalizeaza matricea initiala scazand din ea media fiecarui rand.
  u = zeros(m, 1);
  for i = 1 : m 
    u(i) = sum(photo(i, :)) / n;
    photo(i, :) = photo(i, :) - u(i);
  endfor
  
  % construieste matricea Z.
  Z = photo' / sqrt(n - 1);
  
  % calculeaza matricile U, S si V din SVD aplicat matricii Z
  [U, S, V] = svd(Z);
  
  % construieste matricea W din primele pcs coloane ale lui V
  W = resize(V, m, pcs);
  
  % cacluleaza matricea Y
  Y = W' * photo;
  
  % aproximeaza matricea initiala
  new_X = W * Y + u;
  
  % transforma matricea in uint8 pentru a fi o imagine valida.
  new_X = uint8(new_X);
  
endfunction
