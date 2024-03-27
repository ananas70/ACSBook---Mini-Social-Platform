# ACSBook - Mini-Socialization Platform

To implement this social platform, I used the public classes App (where the main method is located), User, Post, Comment, and FileUtils, as well as the Likeable interface.

## App Class

It retrieves arguments from the command line and passes them to the processCommandLineArgs method, which contains a switch statement used to identify the desired request (create-post, delete-post, etc.).

## User Class

It contains private fields for username, password, number of likes, followers, as well as a static ArrayList (UsersArray) containing all valid users. Get and set methods are implemented to access private members, and methods are used to increment and decrement the number of likes and followers.
The createSystemUser method creates new users, adds them to UsersArray, and writes them to the Users.txt file in the format username,password.
Also implemented here are methods related to Follow, Unfollow, getFollowing, getFollowers, getMostLikedUsers, and getMostFollowedUsers.

## Post Class

It contains private fields for the User who created the post, post content (text), post id, number of likes, post timestamp, as well as a static ArrayList containing all valid Post objects. Get and set methods are implemented.
When a new post is made, the static integer variable idCounter is incremented, and the current post id is assigned.
The createSystemPost method creates new posts, adds them to PostsArray, and writes them to the Posts.txt file in the format USER:username,ID:id,POST:text.
Methods getUserPosts, getPostDetails, getMostLikedPosts, getMostCommentedPosts, getFollowingsPosts are also here.

## Comment Class

It contains private fields for the User who wrote the comment, comment content (text), comment id (implemented similarly to Post with a static counter), number of likes, the post within which the comment is written, as well as a static ArrayList (CommentsArray) containing all valid Comment objects created.
The createSystemComment method creates comments, adds them to CommentsArray, and writes them to the Comments.txt file in the format Comments.txt: USER:username,POST_ID:id,COMMENT_ID:id,COMMENT:text.

## FileUtils Class

This class is used to implement methods related to displaying data according to specified formats, as well as manipulating created text files (checking if it's empty, copying, deleting a file).
The Likeable Interface:
It includes abstract methods like and unlike, as well as already implemented methods verifyAlreadyLiked and unlikeFromFile (which deletes the line corresponding to the like to be removed from the text file).

## Bonus:

Other edge cases:

1. Deleting a user from the database - involves deleting all posts, comments, likes, and follow/unfollow links associated with the user and updating files and data structures.

2. Modifying the content of a post or comment - involves checking rights and updating the respective timestamp as well as the files containing the comment/post and the associated ArrayList. There is no need to modify the number of likes.

3. Changing user authentication data (username and/or password) - involves checking that the new username does not coincide with an existing one, that the password is not empty, or exceeds a size limit. If the username has been updated, it is necessary to update the follow/unfollow links and update its new name in files where it appears (possibly: "PostLikes", "CommentLikes", "Followers").

How I would refactor commands and responses in the application:

1. Using the recently acquired knowledge in Course 9, I would adopt the serialization technique for more efficient data management. Thus, I would save objects (Users, Posts, Comments) directly to a file, eliminating the need to manually parse complex data structures (as done here).

2. Instead of storing in text files, I could refactor to use a database management system (e.g., MySQL) instead of storing data in text files. Possibly, this may involve integrating JSON files.

3. Adding password validation: Users should not be allowed to use a password that is too short or too simple. We could introduce methods for analyzing or even suggesting strong and valid passwords.
