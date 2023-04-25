package com.labshigh.martinipool.scheduler.common.util.models;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelegramMessage {
  public String chat_id;
  public String text;
}
