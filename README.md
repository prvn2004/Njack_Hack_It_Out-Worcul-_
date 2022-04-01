This is a community platform(android application) for programmers to  share their work expereience and projects among their juniors and other programmers to help them.

firstly download the app(apk file) from here.(https://drive.google.com/file/d/19Sry8QoH1i7LOhTtepgjxw-LBf9tJGfZ/view?usp=drivesdk)

then you need to signup using your email address and a confirmation email will be sent to your email after signup. after confirming it, you can login in the app.
you can also login using google account directly(it makes easy to login and manage your account)

so the first page of app contains all the messages sent by peoples. messages are arranged in a way such that new messages will come on top.

the second page is interesting. it uses github repo and github user api to search users with their username and get their details and repos. we will come on this point later, firstly let move on next point.
the third page contains user's profile details and messages done by user. at top there is toolbar containing logout button. session management is done very precisely.!


then lets go on our first page again, on first page there is messaging button which leads us to new page which is very much interesting .
on that page i have used ocr(extract text from image) api. user can upload a image of code and the text will be extracted from that using the api.

that's not all after extracting code/text from image, u can directly upload that code and generate a link for that code and share with community, for link generation i 
have used pastebin api, which is very interesting. 
user can also send images and messages to the community. this is done by using firebase storage and realtime database.

and thats how you can share experiences and code with other programmers.
also you can share the github repos url(link) using github searching feature i mentioned before.

try the app yourself to interect with the community and experience all feature. 
