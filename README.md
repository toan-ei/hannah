# project  Hannah - AI Learning Assistant for Software Engineering Education
## Mô tả
### được viết dựa theo website education dành cho người dạy lập trình và người cần học lập trình, ở trong đây không chỉ học mà các người dùng có thể đăng bài để chia sẽ kiến thức mình có đến với mọi người, web tích hợp thêm sẵn chat với ai nhằm phục vụ cho việc học hành. model mà web sử dụng là genmini bản free của google.
## gồm 6 service backend và 1 file wepapp frontend.
- frontend được biết mới html và javascript thuần.
- backend được viết bằng java và dùng framework Spring.
## cách start project này thế nào
### b1: clone nhánh main về máy.
### b2: trong mỗi service backend là một database khác nhau.
 - identity-service và profile-service là dùng mysql.
 - ![image](https://github.com/user-attachments/assets/08ccaf09-3a88-448c-8188-4f8253974a6f)
 - ai-service, post-service và file-service dùng mongodb.
 - ![image](https://github.com/user-attachments/assets/cc1688e9-613d-404e-80bd-c39174207ae9)
 - còn service gateway-api thì bỏ qua.
 - lưu ý nhớ sửa lại config cho đúng với database ở local các bạn.
### b3: chạy 6 service dưới backend và cuối dùng và chạy file index.html ở foder webapp.
## tới đây là thành công.
