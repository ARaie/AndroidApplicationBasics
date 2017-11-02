## Course Overview
coming soon

## Getting Started
coming soon

## Understanding Activities and Activity Layout Interaction

Continue the NoteKeeper Application.

This module is about:
* Activity UI
	* An activity is a single, focused thing that user can do.
	* Activity have a lifecycle: 
		* Lifecycle calls a series of methods
		* Use the onCreate method to initialize the Activity
* Layout Classes
	* Layout classes provide positioning flexibility
	* Arrange child views
	* Children can include other layout classes
	* Spetcific positioning behavior depends on the layout class
* ConstraintLayout Class
	* Should set horizontal & vertical constraints
* Activity/Layout Relationship
	* Use Java code to create class instances
	* Relationships and properties set in code (NoteActivity)
		* Must load layout: use setContentView
		* Must request layout View references: Use findViewById
	* Layout files: XML files describe View hierarchy
		* Usually created with UI Designer 
* Populating a Spinner
	* Copy four files from /exercise/before file. (Course material)
	* More Java code to NoteActivity(Spinner info comes from pasted files)
		* R class provides important constants: Layout resources - R.layout, View Id's - R.Id

## Working with Activities

* Activity Interaction
	* Create an intent
		* Identifies the desired Activity
		* Can ve just Activity class info
	* Call startActivity
		* Pass the intent
		* Launches Activity matching the intent
	* Choose ListView and match to whole screen then go to java classes

* Describing Operations with Intents
	* Intents need more than just a target

* Reference Types as Intent Extras
	* Use Parselable API
	* describeContents
		* Indicates spetial behaviour
		* Generally can return 0
	* writeToParcel
		* Receives a Parcel instance

## Taking a Deeper Look at Activity Interaction

* Application Activity RelationShip
	* Android is a component-oriented platform
		* Components run within a process
	* Each app has its own protcess
	* App components run in same protcess
* Implicit Intents
	* How to send email from yor app to another app
* Activities with Results
	* For example camera Activity
	* startActivityForResult
	* Parameters received by onActivityResult
		* Ap defined integer identifier
		* Result code
		* Indent - contains activity result
* Acticity Tasks
	* Task is a collection of activities that users interact with when performing a certain job
	* Manage as a stack
		* Known as the back stack
	* Activities added going forward
	* Back button removes Activities
		* Causes Activity to be destroyed

