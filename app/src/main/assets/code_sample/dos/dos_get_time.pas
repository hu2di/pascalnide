program test_time;
uses dos;
var
    a, b, charValue, d : integer;

function print(w: integer): string;
var
    s : string;
begin
    Str(w,s);
    if w < 10 then
        print := '0' + s
    else
        print := s;
end;

begin
    GetTime(a,b,charValue,d);
    WriteLn('Current time');
    WriteLn(print(a),':',print(b),':',print(charValue));
    readln;
end.
{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}
