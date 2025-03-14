extends CharacterBody2D

@onready var ui = get_node("/root/MainScene/CanvasLayer/UI")
@onready var _animated_sprite = $AnimatedSprite2D

const SPEED = 300.0
const JUMP_VELOCITY = -500.0 


var score : int = 0
var has_jumped : bool = false
var is_attack : bool = false
var can_step : bool = true


func _physics_process(delta: float) -> void:
	if is_on_floor():
		has_jumped = false
	# Add the gravity.
	if not is_on_floor():
		velocity += get_gravity() * delta
		if velocity.y > 0:
			if not is_attack:
				_animated_sprite.play("fall")
			if position.y > 1000:
				die()
	handle_inputs()
	move_and_slide()


func handle_inputs():
	
	if Input.is_action_pressed("attack") and is_attack == false:
		is_attack = true
		$Sword.play()
		$AttackTimer.start()
		if is_on_floor():
			_animated_sprite.play("hit")
		else:
			_animated_sprite.play("air_hit")
		
		
	# Handle (double)jump.
	if Input.is_action_just_pressed("jump"):
		if is_on_floor():
			velocity.y = JUMP_VELOCITY
			has_jumped = false
			_animated_sprite.play("jump")
			$Jumping.play()
		elif not has_jumped:
			velocity.y = JUMP_VELOCITY
			has_jumped = true
			_animated_sprite.play("roll")
			$Jumping.play()
		
		
		
	# Get the input direction and handle the movement/deceleration.
	# As good practice, you should replace UI actions with custom gameplay actions.
	
	var direction := Input.get_axis("move_left", "move_right")
	if Input.is_action_pressed("move_left"):
		$HitArea.scale.x *= -1
		_animated_sprite.flip_h = true
	if Input.is_action_pressed("move_right"):
		$HitArea.scale.x *= -1
		_animated_sprite.flip_h = false
	if direction:
		velocity.x = direction * SPEED
		if is_on_floor and velocity.y == 0 and is_attack == false:
			$Footsteps.pitch_scale = randf_range(0.8, 1.2)
			if can_step:
				can_step = false
				$Footsteps.play()
				$Footsteps/StepTimer.start()
			_animated_sprite.play("walk")
	else:
		velocity.x = move_toward(velocity.x, 0, SPEED)
		if velocity.x == 0 and velocity.y == 0 and is_attack == false:
			_animated_sprite.play("idle")
	
# called when hit enemy
func die():
	get_tree().reload_current_scene()
	
func collect_coin(value):
	$CoinCollect.play()
	score += value
	Wallet.add_coins()
	ui.set_score_text(score)


func _on_hit_area_area_entered(area: Area2D) -> void:
	
	if area.is_in_group("enemies") and is_attack == true:		
		area.die()


func _on_attack_timer_timeout() -> void:
	is_attack = false


func _on_step_timer_timeout() -> void:
	can_step = true
