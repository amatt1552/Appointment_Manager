Title: Appointment Manager
Author: Austin Matthews
Email: amat151@wgu.edu
Version: 1.1
Date: 5/7/2021

Description: The purpose of this application is to add and set customers and appointments.


Notes:
----------------------------------------------------------------------------------------------------
Unfortunately this project no longer runs due to depending a database that no longer exists.
There has been an error where the database connection closes after being inactive for too long then doesn't open again. 
I think i've fixed that error, but if you cannot open the reports menu it most likely is caused by that. A restart of the application will always fix it.
Lambdas can be found in the AppointmentManager class. 
The report i decided to use counts the number of appointments a customer has. I would have put it in the customer menu but felt like it would be easier if all the reports were in the same place.
Driver Version is mysql-connector-java-8.0.23


Login Menu:
----------------------------------------------------------------------------------------------------
Remember that user and password is case sensitive.


Main Menu:
----------------------------------------------------------------------------------------------------
Customers leads to a list of customers and allows editing.
Appointments leads to a list of appointments and also allows editing.
Everything dealing with A3f in the requrements is found in the reports.


Customer Menu:
----------------------------------------------------------------------------------------------------
To open a menu to add a customer press the add customer button.
In order to delete or update a customer you must select it in the tableview first.
If you try to delete a customer with appointments a prompt will come up asking if you are sure. If you pick yes then it will delete all the appointments then delete the customer.


Update/Add Customer Menu:
----------------------------------------------------------------------------------------------------
The clear button only clears the customer values on the left.
Added appointments are not technically saved on the database until you press the update/add button at the bottom.
Appointments deleted however, will not be able to be undone even if you cancel. 
Appointment ID always starts as 0 until you press add/update, then it updates accordingly.
A customer does not have to have an appointment.
To know more about the adding/updating of appointments check the Update/Add Appointment Menu section.


Appointment Menu:
----------------------------------------------------------------------------------------------------
All shows all the appointments.
Current month makes it display appointments in the current month.
Current week makes it display appointments in the current week.
If its not in the same year its not current.
To add an appointment press the add appointment button.
In order to delete or update a customer you must select it in the tableview first.


Update/Add Appointment Menu:
----------------------------------------------------------------------------------------------------
The Add/Update Appointment Menus open a new window and disable the previous window.
It is locked as a 24 hour clock for the time being.
The office hours should be displayed in your local time.
The times can be set in the format H:mm or HH:mm eg: 3:33 or 03:33 
The (current) next to user id means its the user you logged in with.


Reports Menu
----------------------------------------------------------------------------------------------------
Everything dealing with A3f in the requrements is found here.
