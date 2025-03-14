extends CanvasLayer

var final_score : int = 0

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	final_score = Wallet.amount
	$NumberLabel.text = str(final_score) + str("/10")
	if final_score == 10:
		$TextLabel.text = str("Another Epic victory. Big win!")
	elif final_score >= 7:
		$TextLabel.text = str("Ah, victory")
	elif final_score >= 4:
		$TextLabel.text = str("Well you can't expect to find them all...")
	elif final_score > 1:
		$TextLabel.text = str("You should've thought of that earlier")
	elif final_score == 1:
		$TextLabel.text = str("Seriously?")


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass


func _on_again_button_pressed() -> void:
	get_tree().change_scene_to_file("res://main_scene.tscn")


func _on_quit_button_pressed() -> void:
	get_tree().quit()
