# Uncaught TypeError: Object has no method 'find' #
## tham khảo ##
https://github.com/jquery/jquery-mobile/issues/3380
## Nguyên nhân ##
Do các file javascript của jquery, jquery ui được include nhiều lần.
## Cách fix ##
Xóa hết những đoạn javascript include file jquery, jquery-ui, ... trong các trang con (tab content), và các tag được sử dụng trong trang con.