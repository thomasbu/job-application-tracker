export enum ApplicationStatus {
  SENT = 'SENT',
  INTERVIEW = 'INTERVIEW',
  REJECTED = 'REJECTED',
  ACCEPTED = 'ACCEPTED',
}

export const APPLICATION_STATUS_LABELS: { [key in ApplicationStatus]: string } = {
  [ApplicationStatus.SENT]: 'Sent',
  [ApplicationStatus.INTERVIEW]: 'Interview',
  [ApplicationStatus.REJECTED]: 'Rejected',
  [ApplicationStatus.ACCEPTED]: 'Accepted',
};

export const APPLICATION_STATUS_COLORS: { [key in ApplicationStatus]: string } = {
  [ApplicationStatus.SENT]: 'primary',
  [ApplicationStatus.INTERVIEW]: 'accent',
  [ApplicationStatus.REJECTED]: 'warn',
  [ApplicationStatus.ACCEPTED]: 'success',
};
