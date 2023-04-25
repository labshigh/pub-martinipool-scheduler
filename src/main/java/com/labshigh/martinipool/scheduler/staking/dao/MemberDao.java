package com.labshigh.martinipool.scheduler.staking.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labshigh.martinipool.scheduler.common.Constants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDao {

  private long uid;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime createdAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime updatedAt;
  @JsonIgnore
  private String password;
  private boolean deletedFlag;
  private boolean usedFlag;
  private String nickname;
  private String email;
  private boolean emailVerifiedFlag;
  private String phoneNumber;
  private boolean phoneVerifiedFlag;
  private boolean emailNewsletterFlag;
  private boolean otpFlag;
  private String nationalCode;
  private String walletAddress;
  private String referrer;

  private String referrerCode;

  private boolean termsOfUse;
  private boolean privacyPolicy;
  private boolean juminFlag;
  private boolean codeFlag;
  private long juminFileId;
  private long codeFileId;
  private String code;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime codeDate;

  private String approveType;
  private Long approveId;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime approveDate;
  private BigDecimal mEth;
  private boolean migratedFlag;
  private Long walletId;
  private boolean imsiPasswordFlag;
  private String cryptobroId;

  // 이관 체크용
  private String migBalance;
  private String address;
  private String userId;



}