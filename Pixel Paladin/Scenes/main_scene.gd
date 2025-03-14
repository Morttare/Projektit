extends Node2D


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	$BackgroundMusic.play()
	Wallet.reset_coins()


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass


func _on_background_music_finished() -> void:
	$BackgroundMusic.play()


func _on_end_point_body_entered(body: Node2D) -> void:
	if body.name == "Player":
		$EndTimer.start()
		$BackgroundMusic.stop()


func _on_end_timer_timeout() -> void:
	get_tree().change_scene_to_file("res://end.tscn")
