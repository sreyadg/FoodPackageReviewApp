var express = require('express');
var router = express.Router();


router.post('/create', function(req, res) {

	var company_product = req.body.company_product;
	var product_name = req.body.product_name;
	var product_description = req.body.product_description;
	var event_start = req.body.event_start;
	var event_end = req.body.event_end;
	var db_name = req.body.db_name;


	// Set our internal DB variable
	var db = req.db;

	// Set our collection
	var collection = db.get(db_name);

	collection.insert({
		"company_product" : company_product,
		"product_name" : product_name,
		"product_description" : product_description,
		"event_start" : event_start,
		"event_end" : event_end
	}, function (err, doc) {
		if (err) {
			res.send("There was a problem adding the information to the database.");
		}
		else {          
			res.send('Added event details!');
		}
	});
});

router.post('/', function(req, res) {

	var eventType = req.body,eventType;


	// Set our internal DB variable
	var db = req.db;

	// Set our collection
	var collection = db.get(eventType);

	collection.find({},{fields: {"product_name":0, "product_description":0, "event_start":0, "event_end":0, "_id":0}},function(e,docs){
    res.send(docs);
    });
    
});





module.exports = router;
