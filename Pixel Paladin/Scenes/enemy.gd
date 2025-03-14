extends Area2D

@export var speed : int = 100
@export var moveDist : int = 100
@onready var startX : float = position.x
@onready var targetX : float = position.x + moveDist
@onready var _animated_slime = $AnimSlime

var is_dead : bool = false

func _physics_process (delta):
	if not is_dead:
		_animated_slime.play("walk")
		# move to the "targetX" position
		position.x = move_to(position.x, targetX, speed * delta)
		# if we're at our target, move in the other direction
		if position.x == targetX:
			if targetX == startX:
				targetX = position.x + moveDist
			else:
				targetX = startX


# moves "current" towards "to" in an increment of "step"
func move_to (current, to, step):
	var new = current
	# are we moving positive?
	if new < to:
		_animated_slime.flip_h = true
		new += step
		if new > to:
			new = to
	# are we moving negative?
	else:
		_animated_slime.flip_h = false
		new -= step
		if new < to:
			new = to
	return new

func die():
	if not is_dead:
		is_dead = true
		$DeathTimer.start()
		_animated_slime.play("die")	
		$CollisionShape2D.disabled = true
		$DeathSound.play()
	
func _on_body_entered(body: Node2D) -> void:
	if body.name == "Player" and is_dead == false:
		body.die()


func _on_death_timer_timeout() -> void:
	_animated_slime.play("dead")
