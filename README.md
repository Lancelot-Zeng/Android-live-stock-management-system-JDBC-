# Android-live-stock-system-management-system-JDBC-


#This project is in order to make an inventory real-time monitoring application for the Brick company. 
#This application will be used for the warehouse forklift drivers and managers of Brick factory. 
#This live-stock system would separate as two parts, which forklift drivers would use android device, and manager would use a webpage to manage the whole thing. 
#The monitoring application will be able to use for the manager (Administrator) to view and change the current stock data, view the workers' attendance. 
#And the forklift drivers (users) can only use to view the current storage, and receive the orders from manager. 



#*Notice:please deploy the database file("sqldb.db") before deploy the Android application.


***
#Each Activities:


1. Login Activity

This page required user to login as a worker. The worker’s account could only be able to create from web page. 
***




Home Activity:
The home page of android device required worker can see different types of bricks,
and each icon can be able to click to check that brick’s stock details. (Type, batch, row, quantity)





***
Add Activity:

This page required worker to make a request of add new bricks. Select the brick type, 
type in the location and batch number, 
and click the Add button to send an add request to administrator with a default quantity.





***
Order Activity

This page will receive all orders that belongs to the login worker. 
Once the user has done what they should do, make a double check, 
and click “yes” to confirm to change the order status.





***
Feedback Activity
User can send a feedback to administrator to tell him what happen. 
***



