extends CanvasLayer

var LabelVisible : bool = false

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	$HowToLabel.hide()
	$Character.play("idle")


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass


func _on_start_pressed() -> void:
	get_tree().change_scene_to_file("res://main_scene.tscn")


func _on_quit_pressed() -> void:
	get_tree().quit()


func _on_how_to_pressed() -> void:
	if not LabelVisible:
		$HowToLabel.show()
		LabelVisible = true
	else:
		$HowToLabel.hide()
		LabelVisible = false
