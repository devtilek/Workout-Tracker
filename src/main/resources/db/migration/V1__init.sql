CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ---------- users ----------
CREATE TABLE users (
                       id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email         VARCHAR(255) UNIQUE NOT NULL,
                       username      VARCHAR(50)  UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------- exercises ----------
CREATE TABLE exercises (
                           id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           name         VARCHAR(100) UNIQUE NOT NULL,
                           muscle_group VARCHAR(50),
                           description  TEXT
);

-- ---------- workout_plans ----------
CREATE TABLE workout_plans (
                               id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               user_id     UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               name        VARCHAR(100) NOT NULL,
                               description TEXT,
                               created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------- plan_exercises ----------
CREATE TABLE plan_exercises (
                                id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                plan_id       UUID NOT NULL REFERENCES workout_plans(id) ON DELETE CASCADE,
                                exercise_id   UUID NOT NULL REFERENCES exercises(id),
                                target_sets   INT,
                                target_reps   INT,
                                target_weight NUMERIC(6,2),
                                order_index   INT,
                                notes         TEXT
);

-- ---------- workout_sessions ----------
CREATE TABLE workout_sessions (
                                  id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                  user_id      UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                  plan_id      UUID REFERENCES workout_plans(id) ON DELETE SET NULL,
                                  name         VARCHAR(100),
                                  started_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  completed_at TIMESTAMP,
                                  notes        TEXT
);

-- ---------- session_sets ----------
CREATE TABLE session_sets (
                              id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              session_id       UUID NOT NULL REFERENCES workout_sessions(id) ON DELETE CASCADE,
                              exercise_id      UUID NOT NULL REFERENCES exercises(id),
                              set_number       INT  NOT NULL,
                              reps             INT,
                              weight           NUMERIC(6,2),
                              duration_seconds INT,
                              distance_meters  NUMERIC(8,2),
                              completed        BOOLEAN NOT NULL DEFAULT FALSE
);

-- ---------- indexes ----------
CREATE INDEX idx_sessions_user   ON workout_sessions(user_id, started_at DESC);
CREATE INDEX idx_sets_session    ON session_sets(session_id);
CREATE INDEX idx_sets_exercise   ON session_sets(exercise_id);
CREATE INDEX idx_plan_exercises  ON plan_exercises(plan_id);

-- ---------- seed: справочник упражнений ----------
INSERT INTO exercises (name, muscle_group) VALUES
                                               ('Bench Press',       'Chest'),
                                               ('Incline Bench Press','Chest'),
                                               ('Dumbbell Fly',      'Chest'),
                                               ('Push-up',           'Chest'),
                                               ('Squat',             'Legs'),
                                               ('Front Squat',       'Legs'),
                                               ('Leg Press',         'Legs'),
                                               ('Romanian Deadlift', 'Legs'),
                                               ('Deadlift',          'Back'),
                                               ('Barbell Row',       'Back'),
                                               ('Pull-up',           'Back'),
                                               ('Lat Pulldown',      'Back'),
                                               ('Overhead Press',    'Shoulders'),
                                               ('Lateral Raise',     'Shoulders'),
                                               ('Face Pull',         'Shoulders'),
                                               ('Bicep Curl',        'Arms'),
                                               ('Hammer Curl',       'Arms'),
                                               ('Tricep Pushdown',   'Arms'),
                                               ('Skull Crusher',     'Arms'),
                                               ('Plank',             'Core'),
                                               ('Hanging Leg Raise', 'Core'),
                                               ('Cable Crunch',      'Core');