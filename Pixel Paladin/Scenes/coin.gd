extends Area2D

@export var value = 1

@onready var _animated_coin = $AnimCoin
var started : bool = false

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	if not started:
		_animated_coin.play("spin")
		started = true


func _on_body_entered(body: Node2D) -> void:
	# if it was player
	if body.name == "Player":
		body.collect_coin(value)
		queue_free()
