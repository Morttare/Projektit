extends Node

var amount : int = 0

func add_coins():
	amount += 1
	
func reset_coins():
	amount = 0

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass
