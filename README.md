# FourTwentyEight
### By Siyun Lee and Timothy Navarro

* Version: 5.65
* Last Updated: 05/11/2018
* Source Code: https://github.com/STimothyDN/FourTwentyEight

# Introduction

Our platonic matchmaking program utilizes a variant of the Metropolis-Hastings Algorithm that we modified  in order to create pairs. Data is fed in through a .csv file obtained through Google Forms, of which a new version can be obtained by simply clicking the download button from the program. Our program also features an in-program text and spreadsheet viewer to view generated pairs, as well as exception handling to avoid user error. A built-in web browser also allows users to fill out the matchmaking form if they haven’t already done so.

# Features
## STATE OF THE ART MATCHMAKING ALGORITHM
* Through filling out 5 simple questions to quantify your personality, as well as how important those questions are to you, the algorithm guarantees compatibility!
* Two iterations of a variant of the Metropolis-Hastings Algorithm are used, once to hone in on the “true compatibility” between two people, and once again to hone in on the “one true match” for a person.
* More information about how the algorithm runs below:)

## UP-TO-DATE INFORMATION
* Have access to the latest “About Me” file, matchmaking data set, or the program itself, through the program!
* The “About Me” automatically updates when you click on the button, and the matchmaking data set and newest version of the program can be easily downloaded from the click of a button! 
* The program automatically checks for new versions upon launch, and if a new version is downloaded, will automatically restart itself.

## INTENSIVE WHOLE SET MATCHING
* FourTwentyEight’s main feature is creating platonic pairs for ALL users who have filled out the matchmaking form distributed to the NCSSM Classes of 2018 and 2019 Facebook page. 
* After matching, pairs are dumped into a folder called “Matchmaking” in both a .txt file and a .csv file.

## QUICK SINGLE PAIR MATCHING
* If you are only interested in finding YOUR one true platonic partner, the program also features a quick “single pair” matching. Simply select your name, and run “Generate Match.”

## BUILT-IN WEB BROWSER
* For users who have yet to fill out the matchmaking form on the website, you can fill it out through our programs built-in browser!

## BUILT-IN SPREADSHEET AND TEXT VIEWER
* For improved accessibility, the program also features a built-in text and spreadsheet viewer, easily changed through the toggle button at the top right of the GUI. The spreadsheet viewer allows users to sort the people and the compatibility score, making it easier for people to find their name, or to find the most compatible and least compatible pairs!

## BUILT-IN EXCEPTION HANDLING
* Our program is dumb proof! Ran the algorithm after forgetting to load the data file? Trying to use the spreadsheet or text viewer when no pairs have been generated? Don’t fear! Our program won’t let you crash it, no matter how hard you try.

# How our Algorithm Works

Every person that fills out our survey essentially scores their personality through five questions on a scale of 1 - 5. They also score how important it is to you for your answer to match or slightly match your potential platonic partner’s answer. These scores are stored in a Google Sheets file, from which our program downloads and stores the data into a .csv format so that the program can read the data. 

When the algorithm begins to first run, the program randomly selects one user and labels that user as “User 1.” The program then generates two different probabilities of User 1 being compatible with the next user in the pool, one compatibility score that does not factor in importance, and another compatibility score that does. Our algorithm iterates through the entire user pool and will generate this pair of probabilities for every single user.

Getting into the actual implementation of the variant of the Metropolis-Hastings algorithm that we utilized, this first iteration of our “Metropolis-Hastings-Lee-Navarro” algorithm, or MHLN algorithm. This iteration aims to discover the “true” probability that any two people are compatible by testing the prior belief against a randomly generated new belief over a long MCMC (Monte Carlo Markov Chain) length. The probability that did not factor in importance is labeled the initial “prior belief”, and the algorithm will generate a “new belief” by firstly taking the probability that two users are compatible, factoring in importance, and then adding or subtracting a value that was randomly generated from a normal distribution with a reasonable standard deviation. The prior and the new belief are now tested against each other by creating a test ratio of the new belief/prior belief, and a comparison ratio randomly generated between 0 and 1. A test ratio greater than 1 indicates that the new belief is more likely to occur than the prior belief, therefore the algorithm will “accept” the new belief, and the new belief becomes the prior belief in the next iteration of the chain. If the test ratio is less than 1, this indicates that the new belief is less likely to occur than the prior belief, therefore the algorithm will “accept” the new belief on the condition that the test ratio is greater than the comparison ratio. If it is not, the algorithm “rejects” the new belief, and the prior belief becomes the prior belief in the next iteration of the chain. After running, the program selects the “true” compatibility to be the belief that occurred as the prior belief the most throughout the MCMC chain. This first implementation iterates through the entire pool of users, generating “true” compatibilities between User 1 and every other user.

The second implementation of the MHLN algorithm aims to discover the “true” platonic match for User 1. With the same logic as above, the initial prior belief is the “true” compatibility of User 1 and User 2, while the new belief is any randomly generated user. The compatibilities are tested again over a long chain, and the program selects the “true” match to be the user that appeared the most as the prior belief.

From here, User 1 and their “true” match gets removed from the pool of users, speeding up future iterations of the algorithm, as the algorithm again randomly selects a User 1 and runs through, until the pool of users is fully exhausted, or in the case of an odd number of total users, the pool is reduced to one person.

