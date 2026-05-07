export enum Category {
  JAVA = 'JAVA',
  SPRING = 'SPRING',
  ANGULAR = 'ANGULAR',
  SQL = 'SQL',
  GIT = 'GIT',
  ALGORITHMS = 'ALGORITHMS',
  DATA_STRUCTURES = 'DATA_STRUCTURES',
  DOCKER = 'DOCKER',
  TESTING = 'TESTING',
  CI_CD = 'CI_CD',
  CLOUD = 'CLOUD',
  OTHER = 'OTHER',
}

export const CATEGORY_LABELS: { [key in Category]: string } = {
  [Category.JAVA]: 'Java',
  [Category.SPRING]: 'Spring',
  [Category.ANGULAR]: 'Angular',
  [Category.SQL]: 'SQL',
  [Category.GIT]: 'Git',
  [Category.ALGORITHMS]: 'Algorithms',
  [Category.DATA_STRUCTURES]: 'Data Structures',
  [Category.DOCKER]: 'Docker',
  [Category.TESTING]: 'Testing',
  [Category.CI_CD]: 'CI/CD',
  [Category.CLOUD]: 'Cloud',
  [Category.OTHER]: 'Other',
};

export enum Difficulty {
  EASY = 'EASY',
  MEDIUM = 'MEDIUM',
  HARD = 'HARD',
}

export const DIFFICULTY_LABELS: { [key in Difficulty]: string } = {
  [Difficulty.EASY]: 'Easy',
  [Difficulty.MEDIUM]: 'Medium',
  [Difficulty.HARD]: 'Hard',
};
