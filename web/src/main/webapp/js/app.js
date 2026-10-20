/**
 * CareerBridge - Client-Side Application JavaScript
 * Provides UI interactivity, client-side validation, alerts, and helpers.
 */

(function () {
  'use strict';

  document.addEventListener('DOMContentLoaded', function () {
    initNavbarToggle();
    initSidebarToggle();
    initAlertDismissal();
    initConfirmDialogs();
    initFormValidation();
    initFileUploadHelpers();
    initCharacterCounters();
    initTableRowNavigation();
    initAutoSubmitFilters();
    initSearchInputs();
    initBackToTop();
  });

  /**
   * 1. Mobile Navbar Toggle
   */
  function initNavbarToggle() {
    var toggler = document.querySelector('.navbar-toggler');
    var collapse = document.querySelector('.navbar-collapse');

    if (!toggler || !collapse) {
      return;
    }

    toggler.addEventListener('click', function (e) {
      e.stopPropagation();
      var isExpanded = collapse.classList.toggle('active');
      toggler.setAttribute('aria-expanded', isExpanded ? 'true' : 'false');
    });

    // Close when clicking outside of the navbar
    document.addEventListener('click', function (e) {
      if (!collapse.contains(e.target) && !toggler.contains(e.target) && collapse.classList.contains('active')) {
        collapse.classList.remove('active');
        toggler.setAttribute('aria-expanded', 'false');
      }
    });

    // Close on Escape key
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && collapse.classList.contains('active')) {
        collapse.classList.remove('active');
        toggler.setAttribute('aria-expanded', 'false');
      }
    });
  }

  /**
   * 2. Sidebar Toggle (Mobile Dashboards)
   */
  function initSidebarToggle() {
    var sidebar = document.querySelector('.sidebar');
    var sidebarToggleBtn = document.querySelector('.sidebar-toggle');

    if (!sidebar || !sidebarToggleBtn) {
      return;
    }

    sidebarToggleBtn.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();
      sidebar.classList.toggle('active');
    });
  }

  /**
   * 3. Alert Dismissal (Auto-close after 5s & Close button click)
   */
  function initAlertDismissal() {
    var alerts = document.querySelectorAll('.alert');

    alerts.forEach(function (alert) {
      // Manual close button
      var closeBtn = alert.querySelector('.alert-close, [data-dismiss="alert"]');
      if (closeBtn) {
        closeBtn.addEventListener('click', function () {
          dismissAlert(alert);
        });
      }

      // Auto dismiss after 5 seconds unless data-persistent="true"
      var isPersistent = alert.getAttribute('data-persistent') === 'true';
      if (!isPersistent) {
        setTimeout(function () {
          dismissAlert(alert);
        }, 5000);
      }
    });

    function dismissAlert(alertEl) {
      if (!alertEl || !alertEl.parentElement) return;
      alertEl.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
      alertEl.style.opacity = '0';
      alertEl.style.transform = 'translateY(-6px)';
      setTimeout(function () {
        if (alertEl.parentElement) {
          alertEl.remove();
        }
      }, 400);
    }
  }

  /**
   * 4. Confirmation Dialogs for Destructive Actions
   */
  function initConfirmDialogs() {
    document.addEventListener('click', function (e) {
      var target = e.target.closest('[data-confirm], .confirm-action');
      if (!target) return;

      var message = target.getAttribute('data-confirm') || 'Are you sure you want to perform this action?';
      var confirmed = window.confirm(message);
      if (!confirmed) {
        e.preventDefault();
        e.stopPropagation();
      }
    });
  }

  /**
   * 5. Client-Side Form Validation Helpers
   */
  function initFormValidation() {
    var forms = document.querySelectorAll('form.needs-validation');

    forms.forEach(function (form) {
      form.addEventListener('submit', function (e) {
        var isValid = validateForm(form);
        if (!isValid) {
          e.preventDefault();
          e.stopPropagation();
        }
      });

      // Clear errors on input change
      form.querySelectorAll('.form-control').forEach(function (input) {
        input.addEventListener('input', function () {
          clearInputError(input);
        });
        input.addEventListener('change', function () {
          clearInputError(input);
        });
      });
    });

    function validateForm(form) {
      var isValid = true;
      var inputs = form.querySelectorAll('input, select, textarea');

      inputs.forEach(function (input) {
        // Skip disabled or hidden inputs
        if (input.disabled || input.type === 'hidden') return;

        // Required field validation
        if (input.hasAttribute('required') && !input.value.trim()) {
          showInputError(input, 'This field is required.');
          isValid = false;
          return;
        }

        // Email validation
        if (input.type === 'email' && input.value.trim()) {
          var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
          if (!emailRegex.test(input.value.trim())) {
            showInputError(input, 'Please enter a valid email address.');
            isValid = false;
            return;
          }
        }

        // Min length validation
        var minLength = input.getAttribute('minlength');
        if (minLength && input.value.length < parseInt(minLength, 10)) {
          showInputError(input, 'Must be at least ' + minLength + ' characters.');
          isValid = false;
          return;
        }

        // Password confirmation match
        var matchTargetId = input.getAttribute('data-matches');
        if (matchTargetId) {
          var matchTarget = document.getElementById(matchTargetId);
          if (matchTarget && input.value !== matchTarget.value) {
            showInputError(input, 'Passwords do not match.');
            isValid = false;
            return;
          }
        }

        // Salary / Number min-max range check
        if (input.type === 'number' && input.value) {
          var numVal = parseFloat(input.value);
          var minVal = input.getAttribute('min');
          var maxVal = input.getAttribute('max');
          if (minVal !== null && numVal < parseFloat(minVal)) {
            showInputError(input, 'Value must be at least ' + minVal + '.');
            isValid = false;
            return;
          }
          if (maxVal !== null && numVal > parseFloat(maxVal)) {
            showInputError(input, 'Value must not exceed ' + maxVal + '.');
            isValid = false;
            return;
          }
        }
      });

      return isValid;
    }

    function showInputError(input, message) {
      input.classList.add('is-invalid');
      input.style.borderColor = 'var(--color-danger)';

      var parent = input.closest('.form-group') || input.parentElement;
      var errorEl = parent.querySelector('.form-error');

      if (!errorEl) {
        errorEl = document.createElement('div');
        errorEl.className = 'form-error';
        parent.appendChild(errorEl);
      }
      errorEl.textContent = message;
      errorEl.style.display = 'block';
    }

    function clearInputError(input) {
      input.classList.remove('is-invalid');
      input.style.borderColor = '';
      var parent = input.closest('.form-group') || input.parentElement;
      var errorEl = parent.querySelector('.form-error');
      if (errorEl) {
        errorEl.textContent = '';
        errorEl.style.display = 'none';
      }
    }
  }

  /**
   * 6. File Upload Helpers (extension & size validation, filename preview)
   */
  function initFileUploadHelpers() {
    var fileInputs = document.querySelectorAll('input[type="file"]');

    fileInputs.forEach(function (fileInput) {
      var wrapper = fileInput.closest('.file-upload-wrapper') || fileInput.parentElement;
      var preview = wrapper.querySelector('.file-upload-preview');

      fileInput.addEventListener('change', function () {
        if (!fileInput.files || fileInput.files.length === 0) {
          if (preview) preview.classList.remove('active');
          return;
        }

        var file = fileInput.files[0];
        var maxSize = parseInt(fileInput.getAttribute('data-max-size') || '5242880', 10); // Default 5MB
        var allowedExts = (fileInput.getAttribute('data-allowed-ext') || 'pdf,doc,docx')
          .toLowerCase()
          .split(',')
          .map(function (ext) { return ext.trim(); });

        var fileName = file.name;
        var fileExt = fileName.split('.').pop().toLowerCase();
        var fileSizeKB = Math.round(file.size / 1024);

        // Check extension
        if (allowedExts.indexOf(fileExt) === -1) {
          alert('Invalid file format. Allowed types: ' + allowedExts.join(', ').toUpperCase());
          fileInput.value = '';
          if (preview) preview.classList.remove('active');
          return;
        }

        // Check size
        if (file.size > maxSize) {
          var maxMB = Math.round(maxSize / (1024 * 1024));
          alert('File is too large. Maximum allowed size is ' + maxMB + 'MB.');
          fileInput.value = '';
          if (preview) preview.classList.remove('active');
          return;
        }

        // Display filename and size
        if (!preview) {
          preview = document.createElement('div');
          preview.className = 'file-upload-preview active';
          wrapper.appendChild(preview);
        } else {
          preview.classList.add('active');
        }

        preview.innerHTML = '<strong>Selected file:</strong> ' + escapeHtml(fileName) + ' (' + fileSizeKB + ' KB)';
      });
    });
  }

  /**
   * 7. Textarea Character Counters
   */
  function initCharacterCounters() {
    var textareas = document.querySelectorAll('textarea[data-max-chars], textarea[maxlength]');

    textareas.forEach(function (ta) {
      var maxChars = parseInt(ta.getAttribute('data-max-chars') || ta.getAttribute('maxlength'), 10);
      if (!maxChars || isNaN(maxChars)) return;

      var parent = ta.closest('.form-group') || ta.parentElement;
      var counter = parent.querySelector('.char-counter');

      if (!counter) {
        counter = document.createElement('div');
        counter.className = 'char-counter';
        ta.parentNode.insertBefore(counter, ta.nextSibling);
      }

      function updateCounter() {
        var currentLen = ta.value.length;
        var remaining = maxChars - currentLen;
        counter.textContent = currentLen + ' / ' + maxChars + ' characters';

        if (remaining < 0) {
          counter.classList.add('limit-exceeded');
        } else {
          counter.classList.remove('limit-exceeded');
        }
      }

      ta.addEventListener('input', updateCounter);
      updateCounter();
    });
  }

  /**
   * 8. Table Row Click Navigation
   */
  function initTableRowNavigation() {
    var rows = document.querySelectorAll('tr[data-href]');

    rows.forEach(function (row) {
      row.style.cursor = 'pointer';
      row.addEventListener('click', function (e) {
        // Do not navigate if user clicked on button, link, form control, or inside .no-nav
        var interactive = e.target.closest('a, button, input, select, textarea, label, .no-nav');
        if (interactive) return;

        var href = row.getAttribute('data-href');
        if (href) {
          window.location.href = href;
        }
      });
    });
  }

  /**
   * 9. Auto-Submit Filter Dropdowns
   */
  function initAutoSubmitFilters() {
    var autoSelects = document.querySelectorAll('select.auto-submit, select[data-auto-submit]');

    autoSelects.forEach(function (sel) {
      sel.addEventListener('change', function () {
        var form = sel.closest('form');
        if (form) {
          form.submit();
        }
      });
    });
  }

  /**
   * 10. Search Inputs & Enter Key Handling
   */
  function initSearchInputs() {
    var searchInputs = document.querySelectorAll('.search-input');

    searchInputs.forEach(function (input) {
      input.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') {
          var form = input.closest('form');
          if (form) {
            e.preventDefault();
            form.submit();
          }
        }
      });
    });
  }

  /**
   * 11. Smooth Scroll To Top Button
   */
  function initBackToTop() {
    var btn = document.querySelector('.back-to-top');

    if (!btn) {
      btn = document.createElement('button');
      btn.className = 'back-to-top';
      btn.setAttribute('aria-label', 'Back to top');
      btn.innerHTML = '&#9650;';
      document.body.appendChild(btn);
    }

    window.addEventListener('scroll', function () {
      if (window.scrollY > 300) {
        btn.classList.add('visible');
      } else {
        btn.classList.remove('visible');
      }
    });

    btn.addEventListener('click', function () {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
    });
  }

  /**
   * Helper: Escape HTML strings to prevent XSS in dynamic insertion
   */
  function escapeHtml(text) {
    if (!text) return '';
    var div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
  }

})();
