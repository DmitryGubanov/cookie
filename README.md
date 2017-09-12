# cookie

A two-person Android game inspired by "relationship coupons". In short, both people provide a set of rewards they're willing to be responsible for, such a washing dishes or taking them out to dinner. For each reward, you can specify a probability for drawing it from the set of rewards as well as the frequency with which that reward can be drawn (once a day, week, month). Then the other person will be able to draw that reward from their periodic lottery (or fortune cookie) and have that reward available for usage.

Currently, plans to enforce abiding to your own rewards or keeping track of how rewards will be used are up in the air. The main goal is getting one-player functionality going before involving anything over the internet.

# Current work in progress
This is a low priority project which gets benched for other, more interesting projects.  

o connect the activities to each other  
o introduce some design to activities  
o connect the register activity to the API  
o connect login activity to the API  
o connect creating lotteries to the API?  
o connect creating rewards to the API  
o connect drawing rewards to the API  
o connect using rewards to the API  

# Long term goals
o play on two separate devices, as opposed to one  
o firebase integration  

# Completed

No usable version yet.

- designed and implemented a database to maintain users, rewards, lotteries, and ownership of rewards and lotteries
- implemented a DBI responsible for performing database operations and returning objects, similar in concept to a REST API
- JUnit tests for the database and DBI
- User, Reward, Lottery object definitions
- implemented an API for the features/functionality in the proposed interface. API includes: registering, logging in, creating rewards, drawing rewards, using rewards, creating lotteries, and updating lotteries
- JUnit tests for the objects and API
- activities and basic layouts using the simplest elements (plain text and plain buttons) available in Android
